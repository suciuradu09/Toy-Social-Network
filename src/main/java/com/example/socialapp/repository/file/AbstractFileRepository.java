/*package com.example.socialapp.repository.file;

import com.example.socialapp.domain.Entity;
import com.example.socialapp.repository.memory.InMemoryRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    String fileName;

    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        loadData(fileName);
    }

    public void loadData(String fileName) {
        try {
            File file = new File(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String linie = myReader.nextLine();
                List<String> attr = Arrays.asList(linie.split(","));
                E e = extractEntity(attr);
                super.save(e);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Eroare la citirea din fisier.\n");
            e.printStackTrace();
        }
    }

    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        writeToFile(super.findAll());
        return e;
    }

    @Override
    public E delete(ID id) {
        E entity = super.findOne(id);
        super.delete(id);
        writeToFile(super.findAll());
        return entity;
    }

    @Override
    public E update(E entity) {
        super.update(entity);
        writeToFile(super.findAll());
        return entity;
    }

    private void writeToFile(Iterable<E> users){
        Path path = Paths.get(fileName);
        try {
            Files.delete(path);
            Files.createFile(path);
        }
        catch(IOException ex) {
            System.out.println("Eroare la citirea fisierului");
            ex.printStackTrace();
        }
        for(E user:users) {
            try {
                String value = createEntityAsString(user);
                Files.writeString(path, value, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                System.out.println("Eroare la citirea fisierului");
                ex.printStackTrace();
            }
        }
    }
}
*/