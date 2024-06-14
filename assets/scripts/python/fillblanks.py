import os

def process_file(input_file, output_file):
    with open(input_file, 'r') as infile, open(output_file, 'w') as outfile:
        for line in infile:
            stripped_line = line.strip()
            if stripped_line:
                outfile.write(f'"{stripped_line}"\n')
            else:
                outfile.write('"_"' + '\n')

# Get the directory where the script is located
script_dir = os.path.dirname(os.path.abspath(__file__))

# Specify the input and output file paths
input_file = os.path.join(script_dir, 'fillblanks_input.txt')
output_file = os.path.join(script_dir, 'fillblanks_output.txt')

# Process the file
process_file(input_file, output_file)

print(f"Processed file has been saved to {output_file}")
