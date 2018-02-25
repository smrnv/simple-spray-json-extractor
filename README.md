### SSJE (simple spray json extractor)

This is a very simple and lightweight extension for [spray-json](https://github.com/spray/spray-json).

It provides convenient DSL for searching and extracting parts of JSON document.
Search performed from the root of the given document and stopped when the first field
with the given name found. So the first field from the nearest level to the root will be returned.  

There are to operators for search:
- `>>` returns `JsValue` or throws exceptions if not found
- `>?` returns `Option[JsValue]`

How it works:

```scala
import spray.json._
import spray.json.DefaultJsonProtocol._
import com.smrnv.ssje.DSL._

val json = """
    {
      "field1": {
        "lvl2": {
          "findMe": "field1-lvl2"
        }
      },
      "array": [
        {"findAnother": "another-array0"},
        {"findAnother": "another-array1"}
      ],
      "field2": {
        "lvl2": {
          "findMe": "field2-lvl2",
          "findAnother": "another-field2-lvl2"
        },
        "findMe": "field2-lvl1"
      },
      "field3": {
        "findMe": "field3-lvl1"
      }
    }
""".parseJson

json >> "findMe" // Returns JsString("field2-lvl1")
json >? "findMe" // Returns Some(JsString("field2-lvl1"))
json >> "lvl2" // Returns JsObject("findMe" -> JsString("field1-lvl2"))
json >> "field3" >> "findMe" // Returns JsString("field3-lvl1")
json >? "findAnother" //Returns Some(JsString("another-array0"))
```

You can combine `>>` and `>?` in any order.
Moreover there is `convertTo[T]` implementation for `Option[JsValue]`, 
it works like the standard method, but returns `Option[T]` value.  

```scala
(json >> "field3" >? "findMe").convertTo[String] // Returns Some("field3-lvl1")
```
