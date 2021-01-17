package practice

object inheritance extends App {

  // abstract
  // can't be instantiated
  abstract class Animal(name: String) {
    val creatureType: String
    def eat: Unit
  }

  class Dog extends Animal("ranger") {
    override val creatureType: String = "Canine"

    override def eat: Unit = println("CRUNCH CRUNCH")

  }

  // TRAITS
  trait Carnivore {
    def eat(animal: Animal): Unit

  }

  class Crocodile extends Animal("Jack") with Carnivore {

    override val creatureType: String = "cock"
    def eat: Unit = "NOM NOM"
    def eat(animal: Animal): Unit = println(s"Im eating ${animal.creatureType}")

  }

   // TRAITS VS ABSTRACT CLASSES
  // 1. TRAITS CAN'T HAVE CONSTRUCTOR PARAMETERS
  // 2. MULTIPLE TRAITS MAY BE INHERITED BY THE SAME CLASS
  // 3. TRAITS = BEHAVIOUR, ABSTRACT CLASS = TYPE OF THING


  // GENERICS IN SCALA

    class MyList[A] {
      // USE THE TYPE A
    }
    val listOfIntegers = new MyList[Int]
    val listOfStrings = new MyList[String]

    object MyList {
      def empty[A]: MyList[A] = ???
    }

    val emptyListOfIntegers = MyList.empty[Int]

    // VARIANCE PROBLEMS
    class Animals
    class Cats extends Animals
    class Dogs extends Animals

     // NOTES ON GENERICS

  // USE THE SAME CODE FOR MULTIPLE (POTENTIALLY UNRELATED) TYPES
  trait Map[key, value] {

  }
  // VARIANCE IF B EXTENDS A, SHOULD LIST[B] EXTENDS LIST[A]
//  trait List[+A]   YES COVARIANT
//  trait List[A]     NO INVARIANT (DEFAULT)
//  trait List[-A]    HELL NO (CONTRAVARIANT)









}
