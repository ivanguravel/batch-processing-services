# batch-processing-services

simple app which is could be used as a application-companion for your website. provides functionality:

- smart calculation of headers. i provided an example for calculating "User-Agent" headers. 
- user processing in batching mode.
- preparing db-2-csv reports.

Consists of 3 parts:
- webapp (could be your website)
- batch processor (spring batch engine)
- data visualization app
- rabbit mq as a message queue
- mysql as a relational database


## Deployment diagram

- ![diagram](pictures/deployment.png "diagram")
