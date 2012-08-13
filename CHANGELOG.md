## CHANGELOG

* 0.4.0
  - BREAKING: "**" route parameter now matches string without leading slash
    (GET on "foo" / **  + "foo/a/b" => "a/b" instead of "/a/b")
