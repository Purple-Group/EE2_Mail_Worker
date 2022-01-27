aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 048155013810.dkr.ecr.eu-west-1.amazonaws.com
@REM call mvn clean package

docker build -t 048155013810.dkr.ecr.eu-west-1.amazonaws.com/ee2/mail_worker:latest .

cd\
cd C:\Dev\Gt247\Environments\EEdocker
docker-compose stop mailworker

 docker-compose up --no-start
 docker-compose start mailworker
 cd C:\Dev\Gt247\code\easyequities\EE2_Mail_Worker



