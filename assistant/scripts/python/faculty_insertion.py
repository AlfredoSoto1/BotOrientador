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
                    WITH contact_insertion AS (
                        INSERT INTO contact(ext, web, phone, facebook, instagram, email)
                        VALUES (%s, %s, %s, %s, %s, %s)
                        RETURNING contid
                    ),
                    department_selected AS (
                        SELECT depid
                        FROM department
                        WHERE abreviation = %s
                    )
                    INSERT INTO faculty(office, name, jobentitlement, description, fdepid, fcontid)
                        SELECT %s, %s, %s, %s, department_selected.depid, contact_insertion.contid
                            FROM department_selected, contact_insertion;
                """, (
                    row['ext'], row['web'], row['phone'], row['facebook'], row['instagram'], row['email'],
                    row['abreviation'],
                    row['office'], row['name'], row['job_entitlement'], row['description']
                ))
                conn.commit()

        # Close the cursor and connection
        cur.close()
        conn.close()

    except Exception as e:
        print(f"An error occurred: {e}")

# Execute the function to insert data from CSV to database
execute_sql_from_csv(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'faculty_insertion.csv'))
