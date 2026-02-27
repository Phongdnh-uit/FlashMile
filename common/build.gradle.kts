dependencies {
    implementation("com.googlecode.libphonenumber:libphonenumber:9.0.24")
	compileOnly("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
	compileOnly("org.springframework.boot:spring-boot-starter")
	compileOnly("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
