# sbt-sound

An [sbt](http://www.scala-sbt.org/) (Simple Build Tool) plugin for adding sounds to sbt's task completions.

This plugin allows you to associate sounds with successful and/or failed task completions, giving you audio feedback. Any TaskKey can have a sound association. This is especially useful for:

* Long build times
* A failing `~compile` that is running in the background/behind other windows
* A failing `~test`
* Drawing your attention to any specific task outcome

The reason I wrote this plugin is that I usually code with a running sbt `~compile` in the background, and would like to know asap when something brakes. I code in [Sublime Text](http://www.sublimetext.com/) (see [my plugin for it](https://github.com/orrsella/sbt-sublime)), and when I'm not connected to an external monitor I have sbt in the background, periodically checking it to see everything's fine. This plugin solves this problem for me, only drawing my attention when the build breaks. I think others could find this plugin useful in other ways as well.

## Add Plugin

To add sbt-sound functionality to your project add the following to your `project/plugins.sbt` file:

```scala
addSbtPlugin("com.orrsella" % "sbt-sound" % "1.0.1")
```

If you want to use it for more than one project, you can add it to your global plugins file, usually found at: `~/.sbt/plugins/plugins.sbt` and then have it available for all sbt projects. See [Using Plugins](http://www.scala-sbt.org/release/docs/Getting-Started/Using-Plugins.html) for additional information on sbt plugins.

### Requirements

* sbt 0.12.x
* Scala 2.9.x, 2.10.x

### Troubleshooting

If you added the plugin globally but still don't have it available, try:

```
$ sbt
> reload plugins
> clean
> reload return
```

Essentially, this enters the `project` project, cleans it, and returns back to your main project (remember that [sbt is recursive](http://www.scala-sbt.org/release/docs/Getting-Started/Full-Def.html#sbt-is-recursive) – each `project/` folder is an sbt project in itself!).

## Usage

To use sbt-sublime, simply enter the `gen-sublime` command in the sbt console to create the project file. When the command is done, open the new Sublime project created to see your own sources and external library sources.

## Functionality

* Creates a `.sublime-project` project file for your project. The default project file created will include the project's base directory and the special external library sources directory. If a project file already exists, the plugin will keep all existing settings in the file and only add the external sources directory. You don't have to worry about losing your Sublime project's settings.

* Automatically fetches sources available for all dependencies.

* Allows fetching all dependencies transitively – have access to the sources of all libraries that your own dependencies require. This can quickly escalate to *a lot* of source code, so the default behavior is to not fetch dependencies transitively, only your direct dependencies (see [next section](https://github.com/orrsella/sbt-sublime#configuration)).

* Works with multi-project build configurations. In this scenario, external libraries will include the dependencies of all projects combined. **Important:** make sure to run the `gen-sublime` command on the root project. Otherwise, you'll create a Sublime project for the sub-project you ran the command on. Not the end of the world, but probably not what you meant to happen.

## Configuration

The following custom sbt settings are used:

* `sublimeExternalSourceDirectoryName` – The name of the directory containing all external library source files. Default value: `External Libraries`.

* `sublimeExternalSourceDirectoryParent` – Where the external library sources directory will be located. Default value: sbt's `target` setting. If left unchanged, running the `clean` command will delete the sources folder. To have it persist, change it's parent away from the target folder.

* `sublimeTransitive` – Indicates whether dependencies should be added transitively (recursively) for all libraries (including the libraries that your own dependencies require – "your dependencies' dependencies"). For large projects, this can amount to dozens of libraries pretty quickly, meaning that *a lot* of code will be searched and handled by Sublime. See if appropriate for your own project. Default value: `false`.

* `sublimeProjectName` – The name of the generated Sublime project file, not including the ".sublime-project" extension. Default value: sbt's `name` setting, that is your project's name as you define it in `build.sbt`.

* `sublimeProjectDir` – Where the generated Sublime project file will be saved. Default value: sbt's `baseDirectory` setting, that is the root of your project. This can be set to anywhere on your machine, it doesn't have to be in the project's root directory (but would be convenient). If you already have a project file, or like to keep all project files together in some special folder, just point there.

To change any/all of these settings (to these arbitrary alternative values), add the following to your `build.sbt` file:

```scala
sublimeExternalSourceDirectoryName := "ext-lib-src"

sublimeExternalSourceDirectoryParent <<= crossTarget

sublimeTransitive := true

sublimeProjectName := "MySublProjectFile"

sublimeProjectDir := new java.io.File("/Users/orr/Dev/Projects")
```

## Notes

* The external library sources directory is considered as artifacts and located by default in `target`, and so running the `clean` command will delete it. But don't worry – you can always re-run `gen-sublime` to get it back, or change `sublimeExternalSourceDirectoryParent` to have it reside out side of the `target` folder and not get deleted during `clean`.

* When running the `gen-sublime` command the existing library sources directory is deleted, and a new one is created.

* All library source files are intentionally marked as read-only so you won't be able to save changes to them. This is mainly to remind you that changing these sources has *absolutely no* effect on the libraries you're using! **This is important** – just because the sources are available doesn't mean they are used in compilation/runtime. These are merely extracted from the source jars for each dependency, as fetched by sbt. If you want to change and edit the external libraries you're using, *this is not the way*. Add them as an sbt project or manually to your own project as source files, to make any changes and compile. Again, this plugin only allows to quickly add the sources to the same Sublime window for convenience purposes only. Sbt doesn't compile *anything* in the `sublimeExternalSourceDirectoryName` folder!

* If you change any of the library dependencies or the specific settings detailed in [Configuration](https://github.com/orrsella/sbt-sublime#configuration), you'll need to reload the sbt project with the `reload` command, and then execute `gen-sublime` again. This will add/remove dependencies' sources accordingly, making sure the list in up-to-date.

* If you change the name of the external sources directory (`sublimeExternalSourceDirectoryName`), you might need to close and re-open the Sublime project for the change to take effect.

* All other Sublime project settings should remain intact when using the plugin, don't be afraid to tweak it if you want.

* Sources, as do dependencies, are usually appropriate for the `scalaVersion` you're using. Changing it and re-running the `gen-sublime` command will update sources accordingly.

* Consider adding the .sublime-project file to `.gitignore` and `file_exclude_patterns` (in sublime's preferences) to not commit and/or display the project file in Sublime, if it's saved to it's default location in the root folder.

## Feedback

Any comments/suggestions? Let me know what you think – I'd love to hear from you. Send pull requests, issues or contact me: [@orrsella](http://twitter.com/orrsella) and [orrsella.com](http://orrsella.com)

## License

This software is licensed under the Apache 2 license, quoted below.

Copyright (c) 2013 Orr Sella

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.