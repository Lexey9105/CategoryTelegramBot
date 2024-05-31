package ru.pavlikov.categorybot.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pavlikov.categorybot.exception.CategoryException;
import ru.pavlikov.categorybot.exception.RootCategoryException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.model.entities.Category;
import ru.pavlikov.categorybot.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;


import static ru.pavlikov.categorybot.constants.Constants.DESC;
import static ru.pavlikov.categorybot.constants.Constants.TRANS;
/**
 * @author pavlikov
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    /**
     * Сохраняет корневую категорию в БД, если она не дублирующаяся, иначе выбрасывает исключение
     * @param command сохраняемая категория
     * @throws RootCategoryException если категория уже существует
     * @author pavlikov
     */
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

    /**
     * Сохраняет дочернюю категорию в БД, если она не дублирующаяся и если еще не создана родительская категория, иначе выбрасывает исключение
     * @param command сохраняемая категория
     * @throws CategoryException если категория уже существует или не создана родительская категория
     * @author pavlikov
     */
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



    /**
     * Сохраняет категории в БД, если они не дублируются, иначе выбрасывает исключение
     * @param command id чата пользователя
     * @param categories сохраняемые категории
     * @throws CategoryException если хотя бы одна из категорий уже существует
     * @author pavlikov
     */
    public void saveAll(Command command,List<Category> categories) {
        if(truthRootCategory(command.getChatId()))
            {
            throw new CategoryException("Древо категорий уже создано, " +
                    "скачайте древо при помощи команды '/download' и удалете древо. После этого повторно загрузите новое");
        }
        categoryRepository.saveAll(categories);
    }
    /**
     * Удаляет все категории из БД по id чата пользователя, если они еще не были удалены, иначе выбрасывает исключение
     * @param command id чата пользователя
     * @throws CategoryException если они еще не были удалены
     * @author pavlikov
     */
    public void deleteAll(Command command){
        if(!truthRootCategory(command.getChatId()))
        {
            throw new CategoryException("Древо категорий уже удалено");
        }

        categoryRepository.deleteAll(findAllByChatId(command));
    }


    /**
     * Возвращает все категории из БД списком по id чата
     * @param command id чата пользователя
     * @throws CategoryException если в БД нет категорий
     * @return {@link List<Category>}
     * @author pavlikov
     */
    public List<Category> findAllByChatId(Command command) {
        if(!truthRootCategory(command.getChatId())){
            throw new CategoryException("Ещё не создано ни одной категрии!");
        }
        return categoryRepository.findCategoryByChatId(command.getChatId());
    }

    /**
     * Удаляет из БД категорию  и все дочерние категории по имени и id владельца
     * @param command имя удаляемой категории и id чата
     * @throws CategoryException если в БД нет категорий
     * @author pavlikov
     */
    @Transactional
    public void removeByNameAndChatId(Command command) {
        String categoryName=extractCategoryName(command.getParentCategory());
        Optional<Category> confirmAbsence=categoryRepository.findCategoryByNameAndChatId(categoryName, command.getChatId());
        if(confirmAbsence.isPresent()){
            categoryRepository.deleteAll(categoryRepository.findCategoriesByParentId(confirmAbsence.get().getId()));
            categoryRepository.deleteCategoryByNameAndChatId(categoryName, command.getChatId());
        }else{
            throw new CategoryException("Категории '" + categoryName + "' не существует!");
        }
    }

    /**
     * Подтверждает наличие корневой категории по id чата
     * @param chatId  id чата
     * @return boolean значение true исли корневая категория есть в БД
     * @author pavlikov
     */
    public boolean truthRootCategory(Long chatId){
        return categoryRepository.existsByChatIdAndParentIsNull(chatId);
    }

    /**
     * Возвращает корневую категорию по id чата
     * @param command  id чата
     * @throws CategoryException если в БД нет категории
     * @return {@link Category}
     * @author pavlikov
     */
    public Category findRootCategory(Command command){
        Optional<Category> rootCategory =categoryRepository.findCategoryByChatIdAndParentIsNull(command.getChatId());
        if(rootCategory.isEmpty()){
            throw new CategoryException("Корневая категория еще не создана!");
        }
        return rootCategory.get();
    }
    /**
     * Возвращает список дочерних  категорий переданного родителя
     * @param parent  родительская категория
     * @return {@link List<Category>}
     * @author pavlikov
     */
    public List<Category> findCategoriesByParen(Category parent){
        return categoryRepository.findCategoriesByParentId(parent.getId());
    }

    /**
     * Возвращает строку с древом категорий
     * @param command  id чата
     * @return {@link String}
     * @author pavlikov
     */
      public  String treeToString(Command command){
         Category rootCategory =findRootCategory(command);
         StringBuilder treeToString=new StringBuilder();
        return createTreeString(rootCategory,DESC,treeToString).toString();
      }

    /**
     * Рекурсивный метод для создания строки с древом категорий
     * @param category  категория записываемая в строку
     * @param descend  строка для создания уровня вложенности категории
     * @param treeToString изменяемая строка для записи категорий
     * @return {@link String}
     * @author pavlikov
     */
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
    /**
     * Обрезает начальный и конченый символ в строке
     * @param category  категория записываемая в строку
     * @return {@link String}
     * @author pavlikov
     */
    private String extractCategoryName(String category) {
        return category.substring(1, category.length() - 1);
    }

}
