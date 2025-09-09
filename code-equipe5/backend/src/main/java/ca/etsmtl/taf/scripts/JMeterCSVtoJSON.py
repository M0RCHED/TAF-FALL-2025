import csv
from datetime import datetime
import json
import argparse

def make_json(csvFilePath, jsonFilePath):
    # Create a dictionary
    data = {
        "module": "jmeter",
        "results": []
    }
    
    # Open a CSV reader called DictReader
    with open(csvFilePath, encoding='utf-8') as csvf:
        csvReader = csv.DictReader(csvf)
        
        # Convert each row into a dictionary and add it to data
        count = 0
        for rows in csvReader:
            # Convert timestamp to date
            if 'timeStamp' in rows:
                timestamp = int(rows['timeStamp']) / 1000  # assuming timestamp is in milliseconds
                date_str = datetime.fromtimestamp(timestamp).strftime('%Y-%m-%d %H:%M:%S')
                rows_with_date = {}

                # Insert fields in the correct order with the new "date" field
                for key, value in rows.items():
                    if key == 'timeStamp':
                        rows_with_date[key] = value
                        rows_with_date['date'] = date_str  # add date field after timestamp
                    else:
                        rows_with_date[key] = value

                data["results"].append(rows_with_date)
            else:
                data["results"].append(rows)

    # Open a JSON writer and use json.dumps() to dump data
    with open(jsonFilePath, 'w', encoding='utf-8') as jsonf:
        jsonf.write(json.dumps(data, indent=4))
        
# Main function to handle arguments
if __name__ == "__main__":
    # Set up argument parser
    parser = argparse.ArgumentParser(description="Convert CSV file to JSON")
    parser.add_argument("csvFilePath", help="Path to the CSV file")
    parser.add_argument("jsonFilePath", help="Path to save the JSON file")
    
    # Parse arguments
    args = parser.parse_args()
    
    # Call the make_json function with provided arguments
    make_json(args.csvFilePath, args.jsonFilePath)
