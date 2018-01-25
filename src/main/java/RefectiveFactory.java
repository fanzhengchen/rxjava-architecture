public class RefectiveFactory {



    public static <T> T newInstance(Class<? extends T> clazz){
        try{
            return clazz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("cannot create instance ");
        }
    }

}
