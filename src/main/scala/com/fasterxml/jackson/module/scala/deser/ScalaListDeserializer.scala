package com.fasterxml.jackson.module.scala.deser

import org.codehaus.jackson.`type`.JavaType
import org.codehaus.jackson.map._
import deser.{CollectionDeserializer, StdDeserializer}
import java.lang.reflect.Constructor
import java.util.Collection
import collection.JavaConversions._
import collection.mutable.ListBuffer
import org.codehaus.jackson._
import collection.JavaConversions

class ScalaListDeserializer(val collectionType: JavaType,
		val valueDeser: JsonDeserializer[Object],
		val valueTypeDeser: TypeDeserializer) extends JsonDeserializer[collection.Iterable[Object]] {

	val constructor: Constructor[Collection[Object]]  = null;
	val javaCollectionDeserializer = new CollectionDeserializer(collectionType, valueDeser, valueTypeDeser, constructor)


	override def deserialize(jp: JsonParser, ctxt: DeserializationContext) : collection.Iterable[Object] = {

		val list = new ListBuffer[Object]()
		val collection = asJavaList(list)
		javaCollectionDeserializer.deserialize(jp, ctxt, collection)

		if (isImmutableTypeRequested) {
			list.toList
		} else {
			list
		}
	}

	private def isImmutableTypeRequested = {
		classOf[collection.immutable.List[Object]].isAssignableFrom(collectionType.getRawClass)
	}
}