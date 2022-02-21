package net.service.impl;

import net.service.ICategoryService;
import net.util.Category;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Override
    public List<Category> getAllCategory() {
        List<Category> cl = loadCategoryXML();
        return cl;
    }

    private List<Category> loadCategoryXML() {
        String csvFile = "resource/LocalData/categories.csv";
        String line = "";
        String cvsSplitBy = ",";

        ClassPathResource classPathResource = new ClassPathResource("LocalData/categories.csv");


        List<Category> cl = new ArrayList<Category>();
        try (BufferedReader br = new BufferedReader(new FileReader(classPathResource.getFile()))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                Category cc = new Category();
                cc.setName(data[1]);
                cc.setTrueName(data[0]);
                cc.setIndex(data[2]);

                cl.add(cc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cl;
    }
}
