package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        /**
         * isAssignableForm 메소드는 특정 class가 특정 class, interface를 상속하거나 구현했는가?
         * instanceof와 같은 기능을 수행하지만, instanceof는 인스턴스화(객체=object)를 대상으로 사용된다.
         *
         */
    }


    @Override
    public void validate(Object target, Errors errors) {

        Item item = (Item) target;


        //검증 로직
        if(!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max",  new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if( resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null );
            }
        }
    }
}
