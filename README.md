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


### Libraries
This project use the following libraries:

    compile 'com.android.support:appcompat-v7:26.+'
    compile 'com.android.support:design:26.0.2'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'io.reactivex.rxjava2:rxjava:2.1.3'
    compile 'com.apollographql.apollo:apollo-rx2-support:0.4.1'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.google.code.gson:gson:2.8.1'
    compile 'com.android.support:recyclerview-v7:26.0.0'
    compile 'com.android.support.constraint:constraint-layout:1.0.2'
    compile 'com.google.android.gms:play-services-places:11.2.0'
    compile 'com.google.android.gms:play-services-location:11.2.0'
    compile 'com.astuetz:pagerslidingtabstrip:1.0.1'
    testCompile 'junit:junit:4.12'
    
    