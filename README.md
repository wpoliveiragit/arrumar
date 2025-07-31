# Tutorial - Spring boot - Contrato OpenAPI - Copy Template
copia um template para o target e geristrar como pacote

## Base
* Crie o diretorio `src\main\template`
* crie uma estrutura assim `<nome-projeto>\br\com\<algum-nome>\gen\nome-projeto`
* crie a estrutura de sua necessidade

## Pom.xml

Adicione a propriedades abaixo
```xml
<properties>
  <gen.from>src/main/template/restful</gen.from>
  <gen.import.restful>br/com/pegasus/gen/restful/**/*.java</gen.import.restful>
  <gen.to>${project.build.directory}/generated-sources/local-project/src/main/java</gen.to>
</properties>
```

Adicione o plugin abaixo
```xml
<plugins>
  <plugin>
    <!-- Importe de pacotes do diretório template [1/2] -->
    <!-- transporta os pacotes desejados para a pasta target -->
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.3.1</version>
    <executions>
      <execution>
        <phase>generate-sources</phase>
        <goals>
          <goal>copy-resources</goal>
        </goals>
        <id>generate-id</id>
        <configuration>
          <resources>
            <resource>
              <directory>${gen.from}</directory>
              <includes>
                <include>${gen.import.restful}</include>
              </includes>
            </resource>
          </resources>
          <outputDirectory>${gen.to}</outputDirectory>
        </configuration>
      </execution>
    </executions>
  </plugin>

  <plugin>
    <!-- Importe de pacotes do diretório template [2/2] -->
    <!-- registra os pacotes importados -->
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>build-helper-maven-plugin</artifactId>
    <version>3.4.0</version>
    <executions>
      <execution>
        <phase>generate-sources</phase>
        <goals>
          <goal>add-source</goal>
        </goals>
        <id>generate-id</id>
        <configuration>
          <sources>
            <source>${gen.to}</source>
          </sources>
        </configuration>
      </execution>
    </executions>
  </plugin>
</plugins>
```

## Explicação
- `gen.from:` Indica ao plugin, onde esta os templates
- `gen.import.restful:` Define qual cadeia de codigo deseja adicionar (pode ser adicionado multiplos `gen.import`. Lembrando q `restful` é o nome do pacote, então ele não pode se repetir)
- `gen.to:` indica ao plugin para onde vai o template e que tudo dentro dele deve ser registrado como pacotes ativos

Obs.: Lembre que se algum pacote possuir algum component do spring, use:
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"<pacote-principa-projeto>", // pacote principal do projeto
		"<pacote-template>" // pacote com os subpacotes do template
})
public class StartApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}

}
```

## Fonte
- https://chatgpt.com/ (para refinamento)