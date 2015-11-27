[![Build Status](https://travis-ci.org/synaptix-labs/toast-tk-engine.svg?branch=master)](https://travis-ci.org/synaptix-labs/toast-tk-engine.svg?branch=master)

# Toast Tk - Engine

Toast TK Engine provides a concise, self-explanatory and type safe way for writing test cases. 
No more “REGEX HELL” :)

## Example:
A scenario action: “Navigate to url *http://www.google.com*”  
Would be written, in Toast, as follows: 
```
Navigate to url {{value:string}} 
```
instead of, 
```
Navigate to url *([\w\W]+)*
```

## Toast TK Engine:
1. Parses a script written in a [markdown format]() to a TestPage
2. Executes the TestPage by locating the best matching [@Action]() within a given [@ActionAdapter]() 
3. Outputs a [Value Based Test Execution Report]()

Toast TK is not another Cucumber or Behave like framework, nor a Gherkin based toolkit. 
it supports BDD or TDD collaborative testing on top of being pluggable to any test driver such as Selenium or Fest. 
It makes test script easier to share between technical team members (Developers) and non-technical ones (Business Analysts) as it overcomes the need for learning a specific scripting syntax.

# How to use Toast Tk Engine

The toast-tk-example project hosts different examples for:
- Web Browser automation
- Rest API automation
- JSON and XML value management as a variable
- Test report generation

# How to contribute

Toast TK is a young open source project. We are looking for passionates, helpful individuals and volunteers to contribute in every single bit of this project: From the website through the documentation to the very core of the application. So, if you like open-source projets and would like to give back some help, we'd like to see your contributions! It doesn't matter how familiar you are with test automation applications, or whether you know how to write programs for Java. There are plenty of ways to be helpful!
One of the first things you should do is actually use Toast TK, and get to know it - read about it, evangelise it, and engage with the wider community. 
If you'd like to help, [get in touch](mailto:sallah.kokaina@synaptix-labs.com) or join us in #toast-tk on freenode and let us know how you'd like to help. We love contributors!! 

# Development workflow
The main development happens on [Github](https://github.com/synaptix-labs/toast-tk-engine). To contribute, [fork](http://help.github.com/fork-a-repo/) the [main repo](https://github.com/synaptix-labs/toast-tk-engine), branch off a [feature branch](https://www.google.com/search?q=git+feature+branches) from master, make your changes and [commit](http://git-scm.com/docs/git-commit) them, [push](http://git-scm.com/docs/git-push) to your fork and submit a [pull request](http://help.github.com/send-pull-requests/).
For the time being and once in a while, we merge pull requests into master, which results in a new snapshot update. 

# Licence

Toast Toolkit (Toast TK)

Copyright 2012-2015 - Synaptix-Labs - http://www.synaptix-labs.com 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
