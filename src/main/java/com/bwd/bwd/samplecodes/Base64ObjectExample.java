package com.bwd.bwd.samplecodes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + '}';
    }
}

public class Base64ObjectExample {

    public static void main(String[] args) {
        // Creating an object
        Person person = new Person("John Doe", 30);

        // Encoding the object to Base64
        String encodedObject = base64EncodeObject(person);
        System.out.println("Encoded Object: " + encodedObject);

        // Decoding the object from Base64
        Person decodedPerson = base64DecodeObject(encodedObject);
        System.out.println("Decoded Object: " + decodedPerson);
    }

    private static String base64EncodeObject(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            // Serialize the object to bytes
            oos.writeObject(object);

            // Encode the bytes to Base64
            byte[] encodedBytes = Base64.getEncoder().encode(baos.toByteArray());

            return new String(encodedBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> T base64DecodeObject(String encodedObject) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(encodedObject));
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            // Deserialize the object from bytes
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}