if (-not (Test-Path -Path "build")) {
    New-Item -ItemType Directory -Path "build" -Force | Out-Null
}

Write-Host "[1/2] Compiling C Backend" -ForegroundColor Blue
gcc -I"$Env:JAVA_HOME/include" -I"$Env:JAVA_HOME/include/win32" -shared -o native/lumen_native_backend.dll src/main/native/test/jni_test.c

Write-Host "[2/2] Instantiating Test" -ForegroundColor Blue
./../gradlew clean test --tests "JNITests.testJNI"