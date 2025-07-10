// Initialize MongoDB with application user
db = db.getSiblingDB('result_management');

// Create application user with read/write permissions
db.createUser({
    user: 'app_user',
    pwd: 'app_secure_password',
    roles: [
        {
            role: 'readWrite',
            db: 'result_management'
        }
    ]
});

// Create sample collections (optional)
db.createCollection('students');
db.createCollection('results');
db.createCollection('subjects');

print('Database initialized successfully');