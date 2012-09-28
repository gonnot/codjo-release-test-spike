Framework codjo.net
===================

This spike is a study for improving the modularity of [codjo-release-test](https://github.com/codjo/codjo-release-test) by introducing a new dynamicaly updatable syntax tree.

### Developer's Note 

1. Install a codjo developer workstation (see [codjo-install-workstation](https://github.com/gonnot/codjo-install-workstation)) - The only required step is for defining codjo's specific maven2 repository (see [codjo setting.xml](https://github.com/gonnot/codjo-install-workstation/blob/master/common/m2/settings.xml) template) 
1. Clone the github project using standard git command or using the codjo script 

		codjo codjo-release-test-spike gonnot

1. Compile the project using the profile codjo (for downloading codjo libraries)

		mvn -P codjo install

### Todo 
* Kernel
  * ~~Create the new syntax tree using drools~~
  * Handle "tag" attribute (e.g. the ```login``` attribute of the ```gui-test``` tag) 
  * Create a generic run engine
* Handle legacy 
  * Read an existing release-test xml script
  * Execute an existing release-test xml script
* Promote new possibilities
  * ~~Generate an XSD~~ 
  * Read a script wrote using a new human readable format
  * Generate a test-release script  
  * Generate documentation from the new syntax tree
  * Improve execution error management
