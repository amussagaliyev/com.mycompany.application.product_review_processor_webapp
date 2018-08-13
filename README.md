# com.mycompany.product_review_processor_webapp
Webapp to run queue processing

To build and run

1. mvn clean package
2. docker build -t mycompany.product_review_processor_webapp .
3. docker run -p 8180:8180 --link mycompany.db:mycompany.db --link mycompany.redis:mycompany.redis --name mycompany.product_review_processor_webapp mycompany.product_review_processor_webapp

To build and push

1. docker build -t mycompany.product_review_processor_webapp .
2. docker tag mycompany.product_review_processor_webapp a142857/mycompany.product_review_processor_webapp
3. docker push a142857/mycompany.product_review_processor_webapp
