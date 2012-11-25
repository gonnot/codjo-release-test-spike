codjo-release-test (spike)
==========================

This spike is a study for improving the modularity of [codjo-release-test](https://github.com/codjo/codjo-release-test) by introducing a new dynamicaly updatable syntax tree.

[![Build Status](https://buildhive.cloudbees.com/job/gonnot/job/codjo-release-test-spike/badge/icon)](https://buildhive.cloudbees.com/job/gonnot/job/codjo-release-test-spike/)

### Developer's Note 

1. Install a codjo developer workstation (see [codjo-install-workstation](https://github.com/gonnot/codjo-install-workstation)) - The only required step is for defining codjo's specific maven2 repository (see [codjo setting.xml](https://github.com/gonnot/codjo-install-workstation/blob/master/common/m2/settings.xml) template) 
1. Clone the github project using standard git command or using the codjo script 

		codjo codjo-release-test-spike gonnot

1. Compile the project 

		mvn install

