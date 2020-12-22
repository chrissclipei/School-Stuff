curl -T tester3 http://localhost:8080 --request-target ABCDEFabcdef012345XYZxyz-mo &
curl -T tester http://localhost:8080 --request-target ABCDEFabcdef012345XYZxyz-mn &
curl -T test http://localhost:8080 --request-target ABCDEFabcdef012345XYZxyz-mq &
curl http://127.0.0.1:8080 --request-target ABCDEFabcdef012345XYZxyz-mn > cmd1 &
curl -T tester2 http://localhost:8080 --request-target ABCDEFabcdef012345XYZxyz-mp &
curl http://127.0.0.1:8080 --request-target ABCDEFabcdef012345XYZxyz-mn > cmd2 &
curl http://127.0.0.1:8080 --request-target ABCDEFabcdef012345XYZxyz-mn > cmd3 &
curl http://127.0.0.1:8080 --request-target ABCDEFabcdef012345XYZxyz-mn > cmd4 &
curl http://127.0.0.1:8080 --request-target ABCDEFabcdef012345XYZxyz-mn > cmd2 &

