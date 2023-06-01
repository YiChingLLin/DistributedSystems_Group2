from confluent_kafka import Producer
from socket import gethostname
from json import dumps
from random import random, randint
from multiprocessing import Process

def run(bus_number:str):
    def delivery_callback(err, msg):
        if err:
            print('ERROR: Message failed delivery: {}'.format(err))
        else:
            print("Produced event to topic {topic}: key = {key:12} value = {value:12}".format(
                topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')))

    while True:
        producer = Producer(
            {'bootstrap.servers': 'localhost:9092', 'client.id': gethostname()})

        producer.produce(topic='G1', key=bus_number, value=dumps({'in': randint(
            0, 1000), 'out': randint(0, 1000)}), callback=delivery_callback)
        producer.poll(random())

p1,p2,p3 = Process(target=run, args=('NCCU-001',)), Process(target=run, args=('NCCU-002',)), Process(target=run, args=('NCCU-003',))

for i in [p1,p2,p3]:
    i.start()
for i in [p1,p2,p3]:
    i.join()
