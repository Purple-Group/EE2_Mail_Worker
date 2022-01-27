package com.ee2.mail_worker.services;

import com.ee2.mail_worker.dao.SubSystemDAO;
import com.ee2.mail_worker.dao.UserDAO;
import com.ee2.mail_worker.dao.entities.SubSystemEntity;
import com.ee2.mail_worker.dao.entities.UsersEntity;
import com.ee2.mail_worker.dto.*;
import com.ee2.mail_worker.dto.reply.UserDTO;
import com.ee2.mail_worker.dto.reply.UserVo;
import com.ee2.mail_worker.dto.request.EmailByTemplateByUserIDRequestDto;
import com.ee2.mail_worker.kafka.producer.MailProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

@Component
@Slf4j
@AllArgsConstructor
public class MailService {

    private final MailProducer mailProducer;
    private final UserDAO userDAO;
    private final SubSystemDAO subSystemDAO;

//    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendMailMessage(EmailByTemplateByUserIDRequestDto emailByTemplateByUserIDRequestDto){

        UsersEntity usersEntity = userDAO.findById(emailByTemplateByUserIDRequestDto.getUserID());
        SubSystemEntity subSystemEntity = subSystemDAO.findById(usersEntity.getSubSystemId());
        UserDTO userDTO = UserDTO.builder()
                .customerCode(usersEntity.getCustomerCode().trim())
                .email(usersEntity.getEmail().trim())
                .firstName(usersEntity.getFirstName().trim())
                .lastName(usersEntity.getLastName().trim())
                .userID(usersEntity.getUserId())
                .subSystem(subSystemEntity.getSubSystem())
                .subSystemID(subSystemEntity.getSubSystemId())
                .build();

        UserVo userVo = UserVo.builder()
                .userId(userDTO.getUserID())
                .customerCode(userDTO.getCustomerCode())
                .subSystem(userDTO.getSubSystem())
                .emailAddress(userDTO.getEmail())
                .subSystemID((int) userDTO.getSubSystemID())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .build();

        mailProducer.sendMailMessage(emailByTemplateByUserIDRequestDto, userVo);
    }

    public String convertToXml(Object message) {
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller;
        try {
            marshaller = new Marshaller(stringWriter);
            marshaller.marshal(message);
        } catch (MarshalException ex) {
            log.error("Did not send  message to queue" + ex.getMessage());
        } catch (ValidationException ex) {
            log.error("Did not send  message to queue" + ex.getMessage());
        } catch (IOException ex) {
            log.error("Did not send message to queue, " + ex.getMessage());
        }
        String myXmlString = stringWriter.toString();
        //  System.out.println(myXmlString);
        return myXmlString;
    }

    private static GTEmailMessageDTO buildEmailMessageDTO (
            MailVo mailVo,
            String emailAddress,
            AttachmentDTO attachmentDTO)
    {

        final String EMAIL_THREAD_ID = "0";
        final String EMAIL_TYP = "0";
        final String SOURCE_ID = "EasyEquities";

        GTEmailMessageDTO gtEmailMessageDTO = new GTEmailMessageDTO();

        gtEmailMessageDTO.setSubject(mailVo.getSubject());
        gtEmailMessageDTO.setEmailThreadID(EMAIL_THREAD_ID);
        gtEmailMessageDTO.setEmailTyp(EMAIL_TYP);

        HeaderMessageDTO headerMessageDTO = new HeaderMessageDTO();
        headerMessageDTO.setSnt(new Date());
        headerMessageDTO.setSourceID(SOURCE_ID);

        headerMessageDTO.setSubsystem(
                mailVo.getUserVo().getSubSystem() == null
                        ? ""
                        : mailVo.getUserVo().getSubSystem().trim());
        headerMessageDTO.setOBID(String.valueOf(mailVo.getUserVo().getSubSystemID()));

        headerMessageDTO.setEmailAddress(emailAddress);
        headerMessageDTO.setTargetID(mailVo.getUserVo().getUserId() + ":" + mailVo.getUserVo().getPID());
        headerMessageDTO.setTLoc(mailVo.getUserVo().getFirstName() + " " + mailVo.getUserVo().getLastName());

        String[] alternateEMailAddresses = mailVo.getUserVo().getAlternateEMailAddresses();
        StringBuffer stringBuffer = new StringBuffer();
        if (alternateEMailAddresses != null && alternateEMailAddresses.length > 0)
        {
            for (int i = 0; i < alternateEMailAddresses.length; i++) {
                stringBuffer.append(alternateEMailAddresses[i]).append(";");
            }
            log.debug("Alternate emails " + stringBuffer.toString() + " for user " + mailVo.getUserVo().getUserId());
        }

        if (mailVo.getBccAddress() != null)
        {
            stringBuffer.append(mailVo.getBccAddress()).append(";");
        }


        headerMessageDTO.setD2Sub(stringBuffer.toString());

        EmailBodyGrpBlockDTO emailBodyGrpBlockDTO = new EmailBodyGrpBlockDTO();
        emailBodyGrpBlockDTO.setValue(transformContent(mailVo));
        emailBodyGrpBlockDTO.setXSLT(mailVo.getIdentifier() + ".xsl");

        gtEmailMessageDTO.setHeaderMessageDTO(headerMessageDTO);
        gtEmailMessageDTO.setEmailBodyGrpBlockDTO(emailBodyGrpBlockDTO);

        gtEmailMessageDTO.setAttachmentDTO(attachmentDTO);

        return gtEmailMessageDTO;
    }


    private static String transformContent(Object xmlObject)
    {
        java.io.StringWriter sw = new java.io.StringWriter();

        if (xmlObject == null) {
            log.error("XMLObject not set");
        }

        try {
            //-- transform the xmlobject to xml
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Marshaller marshaller = new Marshaller(doc);
            marshaller.marshal(xmlObject);

            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);

        } catch (ParserConfigurationException e) {
            log.error(e.getMessage(), e);
        } catch (ValidationException e) {
            log.error(e.getMessage(), e);
        } catch (MarshalException e) {
            log.error(e.getMessage(), e);
        } catch (TransformerConfigurationException e) {
            log.error(e.getMessage(), e);
        } catch (TransformerException e) {
            log.error(e.getMessage(), e);
        }
        //return xml;
        return sw.toString();
    }


}
