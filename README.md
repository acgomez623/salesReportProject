# salesReportProject


## Descripción

El proyecto salesReportProject es una aplicación en Java diseñada para generar informes a partir de datos de ventas y productos. A partir de archivos de entrada (salesmen.txt, products.txt, sales.txt), procesa la información para producir reportes agregados con distintos criterios, como ventas por producto o desempeño de vendedores.

El propósito es automatizar el análisis de datos.


## ⚙️ Requisitos

- **Java JDK** (8).  
- Herramienta para compilar y ejecutar Java (`javac` y `java`, o un IDE como IntelliJ/Eclipse).


## ▶️ Instalación y Ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/acgomez623/salesReportProject.git
   cd salesReportProject

2. Ejecutar clase GenerateInfoFiles.java para la generación alealtoria de archivos de entrada. Esto reescribe los archivos products.txt y salesmen.txt (los crea si no existen) y crea archivos en la carpeta sales/ sobre ventas aletatoria de los vendedores creados en salesmen.txt
3. Ejecutar clase Main.java, se hace la lectura de los archivos generados en el paso dos y genera o sobreescribe en su defecto los archivos report_products.csv y report_salesmen.csv
