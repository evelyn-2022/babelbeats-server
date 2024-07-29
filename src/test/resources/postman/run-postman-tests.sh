#!/bin/bash

# Run Postman collection with Newman
newman run BabelBeats.postman_collection.json -e Prod-BabelBeats.postman_environment.json
