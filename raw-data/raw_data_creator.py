from confluent_kafka import Producer
from json import dumps
from random import random, randint

"""
delivery_callback is a function that is called when a message is delivered.

producer.produce is asynchronous, so this callback will be called from poll or flush.

producer.poll is used to serve delivery reports (on_delivery) from previous produce calls.

Because of windows lacks of fork, I need to create 3 different function to handle the streaming of 3 cars.
https://segmentfault.com/a/1190000013681586
"""
def run1():
    
    def delivery_callback(err, msg):
        if err:
            print('ERROR: Message failed delivery: {}'.format(err))
        else:
            print("Produced event to topic {topic}: key = {key} value = {value}".format(
                topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')))

    current_num = 50
    
    producer = Producer(
        {'bootstrap.servers': 'localhost:9092', 'client.id': 'NCCU-001'})
    
    while True:
        up = randint(0, current_num//2) if current_num > 1 else 50
        down = randint(0, current_num)
        current_num = current_num + up - down

        producer.produce(topic='Roosevelt', key='NCCU-001', value=dumps(
            {'carNumber': 'NCCU-001', 'up': up, 'down': down}), callback=delivery_callback)
        
        producer.poll(random())


def run2():

    def delivery_callback(err, msg):
        if err:
            print('ERROR: Message failed delivery: {}'.format(err))
        else:
            print("Produced event to topic {topic}: key = {key} value = {value}".format(
                topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')))

    current_num = 50

    producer = Producer(
            {'bootstrap.servers': 'localhost:9092', 'client.id': 'NCCU-002'})
    
    while True:
        up = randint(0, current_num//2) if current_num > 1 else 50
        down = randint(0, current_num)
        current_num = current_num + up - down
        
        producer.produce(topic='Roosevelt', key='NCCU-002', value=dumps(
        {'carNumber': 'NCCU-002', 'up': up, 'down': down}), callback=delivery_callback)
        
        producer.poll(random())


def run3():

    def delivery_callback(err, msg):
        if err:
            print('ERROR: Message failed delivery: {}'.format(err))
        else:
            print("Produced event to topic {topic}: key = {key} value = {value}".format(
                topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')))

    current_num = 50
    producer = Producer(
        {'bootstrap.servers': 'localhost:9092', 'client.id': 'NCCU-003'})
    while True:
        up = randint(0, current_num//2) if current_num > 1 else 50
        down = randint(0, current_num)
        current_num = current_num + up - down

        producer.produce(topic='Roosevelt', key='NCCU-003', value=dumps(
            {'carNumber': 'NCCU-003', 'up': up, 'down': down}), callback=delivery_callback)
        
        producer.poll(random())
