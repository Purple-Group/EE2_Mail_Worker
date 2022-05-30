package com.ee2.mail_worker.kafka.consumer;

import com.easybackend.shared.DTO.rest.account_processor.request.TradeDTO;
import com.easybackend.shared.kafka.KafkaTopics;
import com.ee2.mail_worker.services.MailService;
import com.google.gson.Gson;
import com.gt247.exchangemanager.messages.ExchangeCloseMessage;
import com.purplegroup.avro.ClientTradeEventDTO;
import com.purplegroup.avro.TradeRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Component

@Slf4j
public class ClientTradeEventConsumer {
    Gson gson = new Gson();

    @Autowired
    private MailService mailService;

    @KafkaHandler
    @Transactional(transactionManager = "kafkaTransactionManager")
    @KafkaListener(topics = KafkaTopics.TOPIC_CLIENT_TRADE_SUCCESS, containerFactory = "clientTradeEventKafkaListenerContainerFactory")
    public void consumeClientTradeEvent(ConsumerRecord<String, GenericRecord> record) {
        ClientTradeEventDTO clientTradeEventDTO = gson.fromJson(record.value().toString(),ClientTradeEventDTO.class);
        TradeRequestDTO tradeRequestDTO = clientTradeEventDTO.getTradeDTO();
        mailService.sendMailMessageFromTradeSuccessEvent(clientTradeEventDTO);
        TradeDTO tradeDTO = fromTradeRequest(tradeRequestDTO);
        String xml = transformContent(tradeDTO);
        log.info(String.format("Consumed Client Trade Success message -> %s", clientTradeEventDTO));

    }

    private TradeDTO fromTradeRequest(TradeRequestDTO tradeRequestDTO) {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setUserId(tradeRequestDTO.getUserID());
        tradeDTO.setTrustAccountId(tradeRequestDTO.getTrustAccountID());
        tradeDTO.setValueTradeAmount(tradeRequestDTO.getAmount());
        return tradeDTO;
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
