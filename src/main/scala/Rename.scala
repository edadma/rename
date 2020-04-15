package xyz.hyperreal.rename

import java.util.regex.{Matcher, Pattern}
import java.nio.file.{Files, Path, Paths}

import scala.collection.JavaConverters._
import scala.util.matching.Regex

object Rename {

  def apply(pat: Pattern, dir: String, rename: String, test: Boolean, contains: String) = {
    val containsRegex: Regex = if (contains ne null) contains.r else null
    var count                = 0

    for ((p, m) <- Files
                    .walk(Paths.get(dir))
                    .iterator()
                    .asScala
                    .filter(Files.isRegularFile(_))
                    .map(p => (p, pat.matcher(p.toString))) if m.matches) {
      if (rename ne null) {
        if (containsRegex == null || containsRegex.findFirstIn(io.Source.fromFile(p.toFile).mkString).nonEmpty) {
          val path = target(m, rename)

          println(s"${m.group} => $path")

          if (!test)
            Files.move(p, Paths.get(path))

          count += 1
        }
      } else {
        println(m.group)
        count += 1
      }
    }

    println(s"$count file${if (count != 1) "s" else ""} ${if (rename ne null) "renamed" else "found"}")
  }

  val groupRegex = "%([0-9]+)" r

  def target(groups: Matcher, template: String): String = {
    groupRegex.replaceAllIn(template, m => groups.group((m.group(1).toInt)))
  }

}
