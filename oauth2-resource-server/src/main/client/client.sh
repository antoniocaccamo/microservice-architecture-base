#!/bin/sh
TOKEN=`curl -s -u curl_client:password -X POST localhost:8081/oauth/token\?grant_type=client_credentials | egrep -o '[a-f0-9-]{20,}'`
echo "Got token for curl client as : $TOKEN"
curl localhost:8083/product/products -H "Authorization: Bearer $TOKEN"



curl -X POST -vu client:password -H "Accept: application/json" http://localhost:8081/oauth/token -d "password=password&username=product_owner&grant_type=password&scope=read%20write&client_secret=password&client_id=curl_client"