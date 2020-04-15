package xyz.hyperreal.rename

import java.util.regex.Pattern

object Main extends App {

  case class Options(
      test: Boolean = false,
      directory: String = null,
      pattern: Pattern = null,
      rename: String = null,
      contains: String = null,
      exclude: List[String] = Nil
  )

  private val optionsParser = new scopt.OptionParser[Options]("rename") {
    head("Rename", "v0.1.0")
    arg[String]("<search pattern>")
      .action((x, c) => c.copy(pattern = Pattern.compile(x)))
      //      .validate(
      //        x =>
      //          if (!x.exists)
      //            failure(s"not found: $x")
      //          else if (x.isFile && x.canRead)
      //            success
      //          else
      //            failure(s"unreadable: $x"))
      .text("pattern to search for")
    opt[String]('c', "contains")
      .action((x, c) => c.copy(contains = x))
      .text("exclusion pattern")
    opt[String]('d', "dir")
      .action((x, c) => c.copy(directory = x))
      .withFallback(() => ".")
      .text("directory")
    opt[String]('e', "exclude")
      .action((x, c) => c.copy(exclude = c.exclude :+ x))
      .text("exclusion pattern")
    help("help").text("print this usage text").abbr("h")
    opt[String]('r', "ren")
      .action((x, c) => c.copy(rename = x))
      .text("rename pattern")
    opt[Unit]('t', "test")
      .action((_, c) => c.copy(test = true))
      .text("test only, don't rename")
    version("version")
      .text("print the version")
      .abbr("v")
  }

  optionsParser.parse(args, Options()) match {
    case Some(options) =>
      Rename(options.pattern, options.directory, options.rename, options.test, options.contains)
    case None => sys.exit(1)
  }

}
