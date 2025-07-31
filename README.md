# Tutorial - Spring boot - Contrato OpenAPI
Este tutorial visa adicionar um contrato `OpenAPI` a uma aplicação `Spring boot`.

## Pré-requisitos do projeto
- Java 17+
- Maven 3.8+

## Projeto
Se preferir, use https://start.spring.io/ para facilitar  

### Configurações no pom.xml
O parent do deve ser Spring Boot 3.2+  
Obs.: Parent disponivel até a data deste projeto.
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.4</version>
    <relativePath/>
</parent>
```

Adicione as propriedades abaixo.
```xml
<properties>
    <java.version>17</java.version>

    <!-- OPENAPI -->
    <path.openapi>${project.basedir}/src/main/resources/openapi</path.openapi>
    <openapi.inputSpec>${path.openapi}/openapi.yaml</openapi.inputSpec>
    <openapi.apiPackage>br.com.projeto.gen.openapi.api</openapi.apiPackage>
    <openapi.modPackage>br.com.projeto.gen.openapi.type</openapi.modPackage>
</properties>
```

Adicione as dependencias abaixo.
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- OpenApi -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.8.9</version>
    </dependency>
    <dependency>  <!-- Remove vulnerabilidade do 'springdoc-openapi-starter-webmvc-ui' -->
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.18.0</version>
    </dependency>
    <dependency>
        <groupId>org.openapitools</groupId>
        <artifactId>jackson-databind-nullable</artifactId>
        <version>0.2.2</version>
    </dependency>
    <dependency>
        <groupId>javax.validation</groupId>
        <artifactId>validation-api</artifactId>
        <version>2.0.1.Final</version>
    </dependency>
    <dependency>
        <groupId>javax.annotation</groupId>
        <artifactId>javax.annotation-api</artifactId>
        <version>1.3.2</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Adicione o plugin abaixo.
```xml
<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>7.10.0</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <id>openapi</id>
            <configuration>
                <inputSpec>${openapi.inputSpec}</inputSpec>
                <apiPackage>${openapi.apiPackage}</apiPackage>
                <modelPackage>${openapi.modPackage}</modelPackage>
                <templateDirectory>${openapi.tempDirect}</templateDirectory>
                <generatorName>spring</generatorName>
                <configOptions>
                    <interfaceOnly>true</interfaceOnly>
                </configOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### resources
crie a estrutura abaixo
```text
resources\openapi
resources\static\docs
resources\static\<nome-projeto>
resources\application.yml
```
- Em `resources\openapi` adicione o contrato
  - [openapu.yaml](src/main/resources/openapi/openapi.yaml)
- Em `resources\static\docs` crie o arquivo abaixo
  - [license.html](src/main/resources/static/docs/license.html)
  - [terms.html](src/main/resources/static/docs/terms.html)
- em `resources` altere `application.properties` para `application.yml`
  - [application.yml](src/main/resources/application.yml)

## Criação de classes

### OpenApiInfoController
```java
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
public class OpenApiInfoController {

    @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
    public byte[] getContract() throws IOException {
        return Files.readAllBytes(new ClassPathResource("openapi/openapi.yaml").getFile().toPath());
    }
}
```

### implementação dos endpoints
Cada tag do contrato terá uma interface correspondente com o sulfixo 'Api'.
```java
@RestController
public class <nome-classe> implements <nome-tag>Api {
}
```

## Explicação Rápida
O objetivo deste tutorial, é explicar de forma objetiva o que é necessário saber para implementar um projeto openapi.

`pom.xml`
* Spring Boot 3.2+: obrigatório 
* Propriedades: Foram criadas para facilitar a compreenção.
* Dependencias: Dependencias mínimas para um projeto com contratos openapi padrão (sem templates mustaches)
* openapi-generator-maven-plugin: responsavel pela geração das classes necessárias do contrato no target do projeto.
  * `openapi.inputSpec:` Caminho do contrato
  * `openapi.apiPackage:` Diretório onde as classes de controle rest será criado
  * `openapi.modPackage:` Diretório onde as classes de modelos de dados (schemas) serão criados.

`resources e OpenApiInfoController`
- O swagger espera que ao menos exista uma cópia do contrato (com o mesmo nome) em `resources/static/`, mas em geral, foi 
adotado a convenção de o deixar na raiz do `resources` com o nome `openapi.yaml` e isso inativa a área `info` do 
contrato, esse problema é corrigido na classe `OpenApiInfoController`.  
- TODOS os arquivos criados em `resources/static` estão ali por conta que o contrato usado no projeto, usa ele, não tendo 
uma necessidade real.

`Implementação`
- para cada tag criada no contrato openapi, será gerada um interface correspondente com todos os endpoints pertencentes a dela.
- No diretório `target\generate-sources\openapi\src\main\java\` será adicionado os pacotes definidos nas propriedades `openapi.apiPackage` e `openapi.modPackage` no pom.xml 
- Para identificar a interface correspondente a a tag basta ver o sulfixo Api, implemente ela em uma classe com a anotação `org.springframework.web.bind.annotation.RestController`

## Execução
- Execute o comando `mvn clean generate-sources` no console para gerar as classes do target (eu prefiro `mvn clean install -U`.
- Na IDE (se necessário), limpe o cache e sincronize o maven.
- Execute o projeto
- Acesse a URL `http://localhost:8080/swagger-ui/index.html`, o contrato deve aparecer.



## Fonte
- https://willian-kaminski.medium.com/openapi-generator-com-spring-boot-320c47cf3aeb (inicio do estudo)
- https://swagger.io/ (entender a estrutura)
- https://springdoc.org/#springdoc-applications-demos (entender a implementação)
- https://chatgpt.com/ (para refinamento)