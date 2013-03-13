husk
====

Java-based implementations of a bare-bone shell that uses **consoles** to run in multiple
modes.

Aim
---
* A bare-bone shell that provides basic infrastructure to build powerful command-line tools

**This library is in very nascent stage and should NOT be used.**

Usage
-----
Using **husk** is as easy as,

```java
	public static void main(String[] args) {
		HuskShell huskShell = new HuskShell();
		
		huskShell.initialize();
		huskShell.loadExternalCommands("com.example.mytool.commands");
		huskShell.start();

		// we are done running the husk shell
		// shut the shell down
		huskShell.stop();
	}
```

where **com.example.mytool.commands** is the base package root where all your commands are stored. Implementing
a command will need the class to implement the **HuskShellCommand** interface.

Easy, yeah!

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`husk` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------
	
Copyright (c) 2013, Sandeep Gupta

The project uses various other libraries that are subject to their
own license terms. See the distribution libraries or the project
documentation for more details.

The entire source is licensed under the Apache License, Version 2.0 
(the "License"); you may not use this work except in compliance with
the LICENSE. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
