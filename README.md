[![Build Status](https://travis-ci.org/toast-tk/toast-tk-engine.svg?branch=master)](https://travis-ci.org/toast-tk/toast-tk-engine)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=io.toast-tk%3Atoast-tk-engine)](https://sonarqube.com/dashboard/index?id=io.toast-tk%3Atoast-tk-engine) 
[![Technical debt ratio](https://sonarqube.com/api/badges/measure?key=io.toast-tk%3Atoast-tk-engine&metric=sqale_debt_ratio)](https://sonarqube.com/dashboard/index?id=io.toast-tk%3Atoast-tk-engine)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](https://github.com/toast-tk/toast-tk-engine/blob/snapshot/LICENSE.md)

[![Stories in Ready](https://badge.waffle.io/toast-tk/toast-tk-engine.svg?label=ready&title=Ready)](http://waffle.io/toast-tk/toast-tk-engine)
[![Gitter](https://badges.gitter.im/toast-tk/toast-tk-engine.svg)](https://gitter.im/toast-tk/toast-tk-engine?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# Toast-tk-engine

<a href="http://toast-tk.io"><img src="https://github.com/toast-tk/toast-tk-webapp/blob/master/public/images/ToastLogo.png?raw=true" align="left" height="50"></a>
**Toast-tk-engine** is the core automation framework  for your acceptance tests. It provides a concise, self-explanatory and type safe way for writing test cases. No more _“REGEX HELL”_ :bowtie:

## Example
A scenario action: “Navigate to url *http://www.google.com*”  
Would be written, in Toast, as follows: 
```
Navigate to url {{value:string}} 
```
instead of 
```
Navigate to url *([\w\W]+)*
```

## Toast TK Engine
1. Parses a script written in a [markdown format](https://github.com/toast-tk/toast-tk-engine/wiki/how-to-create-a-scenario) to a TestPage
2. Executes the TestPage by locating the best matching [@Action](https://github.com/toast-tk/toast-tk-engine/wiki/how-to-declare-new-actions) within a given [@ActionAdapter](https://github.com/toast-tk/toast-tk-engine/wiki/how-to-declare-new-actions) 
3. Outputs a [Value Based Test Execution Report]()

Toast TK is not another Cucumber / JBehave like framework, nor a Gherkin based toolkit.  
It supports BDD and TDD collaborative testing on top of being pluggable to any test driver of your choice such as Selenium or Fest.  
It makes test scripts easier to share between technical team members (i.e: Developers) and non-technical ones (i.e: Business Analysts) as it overcomes the need for learning a specific scripting syntax.

# Installation
### Using MAVEN

Add Toast Runtime dependency
```
<dependency>
  <groupId>io.toast-tk</groupId>
  <artifactId>toast-tk-runtime</artifactId>
  <version>0.1.4</version>
</dependency>
```

# How to use Toast Tk Engine

The [toast-tk-example](https://github.com/toast-tk/toast-tk-examples) project hosts different examples for:
- Web Browser automation
- Custom sentences
- Rest API automation
- JSON and XML value management as a variable
- Test report generation
- Toast Maven plugin

# Contribution

Toast TK is a young ![Open Source Love](https://badges.frapsoft.com/os/v3/open-source.svg?v=103) project.  

For contribution rules and guidelines, See [CONTRIBUTING.md](https://github.com/toast-tk/toast-tk-engine/blob/snapshot/CONTRIBUTING.md)

If you'd like to help, [get in touch](https://gitter.im/toast-tk/toast-tk-engine) and let us know how you'd like to help. We love contributors!! 

# Licence
_Toast TK regroups multiple open source projects licensed under the Apache Software License 2._
