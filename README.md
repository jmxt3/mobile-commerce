# mobile-commerce
A sample exercise in how to build a mobile commerce

### API link:
    > https://https://803votn6w7.execute-api.us-west-2.amazonaws.com/dev/public/graphql

### Download your schema
You'll need a decently recent version of node to run apollo-codegen. Then from your android project directory, do the following:

    > npm install -g apollo-codegen
    > mkdir -p app/src/main/graphql
    > apollo-codegen download-schema https://803votn6w7.execute-api.us-west-2.amazonaws.com/dev/public/graphql --output /Users/joaopmmachete/AndroidStudioProjects/Beer/app/src/main/graphql/com/zxventures/beer/schema.json
    
### Describe your query
Now create a file under **app/src/main/example.graphql** and type your sql query


    
    