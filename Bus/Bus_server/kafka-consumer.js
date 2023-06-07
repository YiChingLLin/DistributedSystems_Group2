
const { Kafka } = require('kafkajs')
const server = require('fastify')()
const kafka = new Kafka({
  clientId: 'my-app',
  brokers: ['localhost:9092']
})


server.register(require('fastify-cors'), { 
  origin: "*",
  methods: ["GET"]
})

let data=[]
let output =[]

const consumer = kafka.consumer({ groupId: 'bus-consumer-group' })
const run = async () => {
    // Consuming
    await consumer.connect()
    await consumer.subscribe({ topic: 'Roosevelt', fromBeginning: true })
    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
          console.log({
            partition,
            offset: message.offset,
            value: message.value.toString(),
          })
          data.push(message.value.toString())
        },
      })
    }
    
run().catch(console.error)

server.get('/data', async function (req, res) {
    output = data
    data = []
    return output
});

server.listen(3001, "127.0.0.1");