# modern

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

To optimize the modern client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test

UI end-to-end tests are powered by [Protractor][], which is built on top of WebDriverJS. They're located in `src/test/javascript/e2e`
and can be run by starting Spring Boot in one terminal (`mvn spring-boot:run`) and running the tests (`grunt itest`) in a second one.

# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `modern`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/modern.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
    * Execute Shell / Command:
        ````
        mvn spring-boot:run &
        bootPid=$!
        sleep 30s
        grunt itest
        kill $bootPid
        ````
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml,build/reports/e2e/*.xml`

# To deploy to Tutum

* Build a Docker image (`aciworldwide/modern`) that will run in a Tutum cluster `mvn clean -DskipTests package spring-boot:repackage docker:build`
* Tag the local image for the Tutum remote repository `docker tag aciworldwide/modern tutum.co/nogbadthebad/modern:v5` (pick your image name and version)
* Push the new Docker image to Tutum `docker push tutum.co/nogbadthebad/modern:v5`
* Use the following Tutum stack for a blue / green deployment with a PostgreSQL database
````
database:
  image: 'postgres:latest'
  environment:
    - POSTGRES_PASSWORD=
    - POSTGRES_USER=modern
  ports:
    - '5432:5432'
lb:
  image: 'tutum/haproxy:latest'
  links:
    - web-green
  ports:
    - '8080:8080'
  restart: always
  roles:
    - global
web-blue:
  image: 'tutum.co/nogbadthebad/modern:v5'
  deployment_strategy: high_availability
  links:
    - database
  restart: always
web-green:
  image: 'tutum.co/nogbadthebad/modern:v5'
  deployment_strategy: high_availability
  links:
    - database
  restart: always
  target_num_containers: 3
````

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
