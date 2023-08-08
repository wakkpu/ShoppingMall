package main;

import Entity.Category;
import Entity.Item;
import dto.CategoryDto;
import service.CategoryService;
import service.ItemService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ItemMain {
    ItemService itemService = new ItemService();
    CategoryService categoryService = new CategoryService();

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void goItem() throws IOException {
        while(true) {
            System.out.println("1. 상품 목록 조회 | 2.종료");
            int choiceNumber = Integer.parseInt(br.readLine());
            if(choiceNumber==2) break;
            if (choiceNumber == 1) {
                searchItem();
            }
        }
    }

    public void searchItem() throws IOException {
		/*
				5. 상품 목록 조회 // read
				단순 조회 - 전체 상품 목록을 쭉 뿌리면
				조건별 검색 - 분류별 검색?
		 */
        System.out.println("1. 단순조회 | 2. 조건검색 | 3. 끝내기");
        long choiceNumber = Long.parseLong(br.readLine());
        if(choiceNumber==1){
            List<Item> items = itemService.selectAll();
            for(Item item : items){
                System.out.println(item.getItemName()+" "+item.getItemPrice());
            }
        }else if(choiceNumber==2){
            List<Category> categories = categoryService.selectAll();

            for(Category category : categories){
                System.out.print(category.getCategoryId()+" "+category.getCategoryName()+" ");
            }
            System.out.println();

            while(true) {
                System.out.println();
                System.out.println("1.보기 2.다음으로감(있으면) 3. 종료");
                choiceNumber = Integer.parseInt(br.readLine());

                if (choiceNumber == 1) {
                       System.out.println("코드 입력");
                    choiceNumber = Integer.parseInt(br.readLine());
                    List<Item> items = categoryService.selectWithCategoryId(choiceNumber);
                    for (Item item : items) {
                        System.out.println(item.getItemName() + " " + item.getItemPrice());
                    }
                } else if (choiceNumber == 2) {
                    System.out.println("상세 정보 볼 코드 입력");
                    choiceNumber = Integer.parseInt(br.readLine());
                    List<CategoryDto> childCategoryId = categoryService.getChildCategoryId(choiceNumber);
                    for (CategoryDto categoryDto : childCategoryId) {
                        System.out.println(categoryDto.getCategoryId()+" "+categoryDto.getCategoryName());
                    }
                } else {
                    break;
                }
            }
        }else{
            return;
        }
    }
}
