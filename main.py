import csv
from datetime import datetime, timedelta
import random

with open('network_traffic.csv','w') as file:
    writer = csv.writer(file)
    writer.writerow(['timestamp','value'])
    cur = datetime.now()
    for i in range(0,10000):
        new_time = cur + timedelta(minutes=i)
        new_time.strftime('%Y-%m-%d %H:%M:%S')
        writer.writerow([new_time.strftime('%Y-%m-%d %H:%M:%S'),random.randint(10,20)])