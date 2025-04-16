import json

with open('./courses.json', 'r') as file:
    courses = json.load(file)

with open('./users.json', 'r') as file:
    users = json.load(file)

with open('./announcements.json', 'r') as file:
    announcements = json.load(file) 

with open('./messages.json', 'r') as file:
    messages = json.load(file)  

with open('./messages.json', 'w') as file:
    json.dump(messages, file, indent=4) 

with open('./announcements.json', 'w') as file:
    json.dump(announcements, file, indent=4)

with open('./courses.json', 'w') as file:
    json.dump(courses, file, indent=4)

with open('./users.json', 'w') as file:
    json.dump(users, file, indent=4)

