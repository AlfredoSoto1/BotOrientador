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
        with open(csv_filename, 'r') as csvfile:
            reader = csv.DictReader(csvfile)
            for row in reader:
                # Insert into contact table and return contid
                cur.execute("""
                    with contact_insertion as (
                        insert into contact(ext, web, phone, facebook, instagram, email)
                            values('_', %s, '_', %s, %s, %s)
                        returning contid
                    )
                    insert into project(name, description, fcontid)
                        select  %s, %s, contact_insertion.contid
                            from contact_insertion;
                """, (
                    row['web'], row['facebook'], row['instagram'], row['email'],
                    row['name'], row['description']
                ))
                conn.commit()

        # Close the cursor and connection
        cur.close()
        conn.close()

    except Exception as e:
        print(f"An error occurred: {e}")

# Execute the function to insert data from CSV to database
execute_sql_from_csv(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'project_insertion.csv'))
