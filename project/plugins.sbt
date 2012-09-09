resolvers += "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo"

resolvers += "scalajars.org repository" at "http://scalajars.org/repository"

addSbtPlugin("reaktor" %% "sbt-scct" % "0.2-SNAPSHOT")

addSbtPlugin("org.scalajars" %% "sbt-scalajars" % "0.1.0")
