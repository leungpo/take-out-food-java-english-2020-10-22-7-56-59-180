import java.util.ArrayList;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        List<Item>itemList = this.itemRepository.findAll();;
        List<SalesPromotion> promotions = this.salesPromotionRepository.findAll();
        String result = "============= Order details =============\n";
        String promotionText = "";
        boolean is30Save6yuan = false;
        double saveMoney = 0;

        List<String> promotionsItem = new ArrayList<String>();
        double price = 0;
        for(String input: inputs){
            String[] inputList = input.split(" ");
            String id = inputList[0];
            int quantity = Integer.parseInt(inputList[2]);
            for(Item item:itemList){
                if(item.getId().equals(id)){
                    result += item.getName() + " x " + quantity + " = " + (int)(item.getPrice() * quantity) + " yuan\n";
                    for(SalesPromotion promotion: promotions){
                        if(promotion.getType().equals("BUY_30_SAVE_6_YUAN")){
                            is30Save6yuan = true;
                        }
                        else if(promotion.getType().equals("50%_DISCOUNT_ON_SPECIFIED_ITEMS")){
                            if(promotion.getRelatedItems().contains(item.getId())) {
                                promotionsItem.add(item.getName());
                                saveMoney += item.getPrice()/2 * quantity;
                            }
                        }
                    }
                    price += item.getPrice() * quantity;
                }
            }
        }
        result += "-----------------------------------\n";
        if(price < 30){
            is30Save6yuan = false;
        }
        if(is30Save6yuan && !promotionsItem.isEmpty()){
            if((price - 6) > (price - saveMoney)){
                price -= saveMoney;
                is30Save6yuan = false;
            }
            else{
                price -= 6;
                promotionsItem.clear();
            }
        }

        if(!promotionsItem.isEmpty()){
            promotionText += "Promotion used:\n" + "Half price for certain dishes (";
            for (int i = 0; i < promotionsItem.size();i++){
                promotionText += promotionsItem.get(i);
                if(i != promotionsItem.size() - 1){
                    promotionText += "，" ;
                }
            }
            promotionText += ")，saving " + (int)saveMoney +" yuan\n" + "-----------------------------------\n";

        }
        else if(is30Save6yuan){
            promotionText += "Promotion used:\n" +
                    "Deduct 6 yuan when the order reaches 30 yuan, saving 6 yuan\n" +
                    "-----------------------------------\n";
        }

        result += promotionText  + "Total：" + (int)price + " yuan\n" +
                "===================================";

        return result;

    }
}
