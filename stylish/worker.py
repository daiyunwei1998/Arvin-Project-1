import redis
import mysql.connector
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import List, Dict
from collections import defaultdict
from decimal import Decimal
import json

# Redis configuration
REDIS_HOST = 'localhost'
REDIS_PORT = 6379
REDIS_PASSWORD = '' # set this
REDIS_LIST_KEY = 'orderProcessTasks'

# MySQL database configuration
MYSQL_HOST = 'localhost'
MYSQL_USER = 'root'
MYSQL_PASSWORD = ''  # set this
MYSQL_DATABASE = 'stylish'

# Gmail SMTP configuration
SMTP_SERVER = 'smtp.gmail.com'
SMTP_PORT = 587
SMTP_USER = 'daiyunwei1998@gmail.com'
SMTP_PASSWORD = ''  # set this
EMAIL_TO = 'daiyunwei1998@gmail.com'
EMAIL_FROM = 'daiyunwei1998@gmail.com'
EMAIL_SUBJECT = 'Order Aggregation Report'


def get_email_from_redis() -> str:
    r = redis.StrictRedis(
        host=REDIS_HOST,
        port=REDIS_PORT,
        password=REDIS_PASSWORD,
        decode_responses=True
    )

    # Get the first item from the list
    json_data = r.lpop(REDIS_LIST_KEY)

    if json_data:
        try:
            print(f"Raw JSON data from Redis: {json_data}")
            # Deserialize the JSON data
            data = json.loads(json.loads(json_data))
            print(f"Deserialized data: {data}")
            print(f"Type of deserialized data: {type(data)}")
            # Extract and return the email field
            email = data.get('email')
            if email:
                return email
            else:
                raise Exception("No 'email' field found in JSON data")
        except json.JSONDecodeError:
            raise Exception("Error decoding JSON data from Redis")
    else:
        raise Exception("No data found in Redis list")

def get_orders_total() -> List[Dict[str, Decimal]]:
    conn = mysql.connector.connect(
        host=MYSQL_HOST,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD,
        database=MYSQL_DATABASE
    )
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT user_id, total FROM orders")
    rows = cursor.fetchall()
    conn.close()

    orders = []
    for row in rows:
        orders.append({
            'user_id': row['user_id'],
            'total': Decimal(row['total'])
        })
    return orders

def calculate_totals(orders: List[Dict[str, Decimal]]) -> Dict[str, Decimal]:
    aggregated_totals = defaultdict(Decimal)
    
    for order in orders:
        user_id = order['user_id']
        total = order['total']
        aggregated_totals[user_id] += total

    return aggregated_totals

def send_email(subject: str, body: str):
    msg = MIMEMultipart()
    msg['From'] = EMAIL_FROM
    msg['To'] = EMAIL_TO
    msg['Subject'] = subject

    msg.attach(MIMEText(body, 'plain'))

    try:
        with smtplib.SMTP(SMTP_SERVER, SMTP_PORT) as server:
            server.starttls()  # Upgrade the connection to a secure encrypted SSL/TLS connection
            server.login(SMTP_USER, SMTP_PASSWORD)
            server.send_message(msg)
        print("Email sent successfully.")
    except Exception as e:
        print(f"An error occurred: {e}")

def main():
    try:
        email = get_email_from_redis()
        print(f"Email retrieved from Redis: {email}")

        orders = get_orders_total()
        totals = calculate_totals(orders)

        # Create email body
        body = "Order Aggregation Report\n\n"
        for user_id, total in totals.items():
            body += f"User ID: {user_id}, Total Payment: {total}\n"

        send_email(EMAIL_SUBJECT, body)
        print("Email sent successfully.")

    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    main()