import os
import csv
import psycopg2

def execute_sql_from_csv(csv_filename):
    try:
        # Connect to the PostgreSQL database
        conn = psycopg2.connect(
            dbname="sodb",
            user="username",
            password="password",
            host="localhost",
            port="5432"
        )
        cur = conn.cursor()

        # Read the CSV file
        with open(csv_filename, 'r', encoding='utf-8') as csvfile:
            reader = csv.DictReader(csvfile)
            for row in reader:
                # Insert into contact table and return contid
                cur.execute("""
                    update building set gpin = %s where code = %s
                """, (
                    row['gpin'], row['code']
                ))
                conn.commit()

        # Close the cursor and connection
        cur.close()
        conn.close()

    except Exception as e:
        print(f"An error occurred: {e}")

# Execute the function to insert data from CSV to database
execute_sql_from_csv(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'googlepin_insertion.csv'))
