package ru.pavlikov.categorybot.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pavlikov.categorybot.exception.CategoryException;
import ru.pavlikov.categorybot.exception.RootCategoryException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.model.entities.Category;
import ru.pavlikov.categorybot.repository.CategoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String DESC = "**";
    private static final String TRANS = "->";

    //-----------------API START-----------------


    public void saveRootCategory(Command command) {
        if(truthRootCategory(command.getChatId())){
            throw new RootCategoryException();
        }
        String rootName=command.getParentCategory().substring( 1, command.getParentCategory().length() - 1 );
        Category category=new Category();
        category.setChatId(command.getChatId());
        category.setName(rootName);
        categoryRepository.save(category);
    }

    public void saveChildrenCategory(Command command) {
        String childrenName=extractCategoryName(command.getChildrenCategory());
        Optional<Category> parent=categoryRepository.findCategoryByNameAndChatId(extractCategoryName(command.getParentCategory()),command.getChatId());
        Optional<Category> children=categoryRepository.findCategoryByNameAndChatId(childrenName,command.getChatId());
        if (parent.isEmpty()){
            throw new CategoryException("Корневой категории '" + command.getParentCategory() + "' не существует!");
        }
        if (children.isPresent()){
            throw new CategoryException("Категория '" + command.getChildrenCategory() + "' уже существует!");
        }
        Category category=new Category();
        category.setChatId(command.getChatId());
        category.setName(childrenName);
        category.setParent(parent.get());

        categoryRepository.save(category);
    }



    public void saveAll(List<Category> categories) {
        if (!categoryRepository.findCategoriesByNameIn(categories.stream()
                .map(Category::getName)
                .toList()).isEmpty()) {
            throw new CategoryException("Одна или несколько категорий уже существуют!");
        }
        categoryRepository.saveAll(categories);
    }


    public List<Category> findAllByChatId(long chatId) {
        return categoryRepository.findCategoryByChatId(chatId);
    }

    @Transactional
    public void removeByNameAndChatId(Command command, long chatId) {
        String categoryName=extractCategoryName(command.getParentCategory());
        Optional<Category> confirmAbsence=categoryRepository.findCategoryByNameAndChatId(categoryName,chatId);
        if(confirmAbsence.isPresent()){
            categoryRepository.deleteAll(categoryRepository.findCategoriesByParentId(confirmAbsence.get().getId()));
            categoryRepository.deleteCategoryByNameAndChatId(categoryName, chatId);
        }else{
            throw new CategoryException("Категории '" + categoryName + "' не существует!");
        }
    }

    public boolean truthRootCategory(Long chatId){
        return categoryRepository.existsByChatIdAndParentIsNull(chatId);
    }

    private Map<Category,String> categoryMap(Long chatId){
        List<Category>categories=findAllByChatId(chatId);
        return categories.stream()
                .collect(Collectors.toMap(category -> category, Category::getName));

    }

      public  String treeToString(Long chatId){
         Optional<Category> rootCategory =categoryRepository.findCategoryByChatIdAndParentIsNull(chatId);
         if(rootCategory.isEmpty()){
             throw new CategoryException("Корневая категория еще не создана!");
         }
         Map<Category,String> categoryMap=categoryMap(chatId);
         StringBuilder treeToString=new StringBuilder();
         //treeToString.append(DESC).
        return createTreeString(rootCategory.get(),DESC,treeToString).toString();
      }

      private StringBuilder createTreeString(Category category,String descend,StringBuilder treeToString){

        treeToString.append(descend).append(category.getName());
        if(categoryRepository.hasChildren(category)) {
            treeToString.append(TRANS).append("\n");

            categoryRepository.findCategoriesByParentId(category.getId()).forEach(category1 -> {
                        createTreeString(category1, descend.concat(DESC), treeToString);
                    }
            );
            return treeToString;
        }else{
            return treeToString.append("\n");
        }


      }

    private String extractCategoryName(String category) {
        return category.substring(1, category.length() - 1);
    }

}
