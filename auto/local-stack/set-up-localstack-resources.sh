awslocal --endpoint-url=http://localhost:4566 dynamodb create-table --table-name demo-table --attribute-definitions AttributeName=id,AttributeType=S --key-schema  AttributeName=id,KeyType=HASH --billing-mode PAY_PER_REQUEST

awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name demo-queue.fifo \
         --attributes \
         "{
           \"ReceiveMessageWaitTimeSeconds\": \"3\",
           \"FifoQueue\": \"true\"
         }"