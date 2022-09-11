# brandwatch API tets

1. Install IntelliJ
2. Install Java (I used Corretto)
2. in the terminal execute the following:
    a. chmod +x ./gradlew
    b. ./gradlew :test ${{ env.tests }}
        i.e --tests "com.bw.test.GoRestAPITests"
# brandwatch
