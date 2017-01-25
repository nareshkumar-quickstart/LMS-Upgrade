package com.softech.vu360.lms.aspects.audit;



/**
 * Audit service for {@link com.softech.vu360.lms.selfservice.services
 * .endpoint.OrderServiceEndpoint}
 *
 * @author ramiz.uddin
 * @since 0.1
 */
//@Aspect
public class OrderServiceEndpoint {

//    private static final Logger LOGGER = Logger.getLogger(
//            OrderServiceEndpoint.class.getName()
//    );
//
//    // OrderService bean
//    private OrderService orderService;
//
//    public void setOrderService(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    public OrderService getOrderService() {
//        return orderService;
//    }
//
//    /**
//     * An interceptor of
//     * {@link com.softech.vu360.lms.selfservice.services.endpoint
//     * .OrderServiceEndpoint#submitOrder}
//     *
//     * <p>
//     * When a SOAP message receives from a client, without even validating it
//     * against its Schema SOAP message will be committed into the database.
//     * However, SOAP messages which will not be able to validated against its
//     * Schema will be instantly responded as failed order but still its
//     * persistence would be required to see the cause of Order failures
//     * </p>
//     *
//     * @param joinPoint
//     * @throws Exception
//     */
//    @Around("execution(* com.softech.vu360.lms.selfservice.services.endpoint" +
//            ".OrderServiceEndpoint.submitOrder(..))")
//    public void submitOrder_before(ProceedingJoinPoint joinPoint) throws
//            Throwable {
//
//        // method arguments
//        Object[] args = joinPoint.getArgs();
//
//        // validate arguments
//        if (args.length == 0)
//            throw new IllegalArgumentException();
//
//        // get only first argument from list and ignore the result
//        Object object = args[0];
//
//        // argument should be <code>OrderCreatedRequest</code> type
//        if (!(object instanceof OrderCreatedRequest))
//            throw new IllegalArgumentException();
//
//        String soapString = marshallArgument(object);
//
//        LOGGER.info("SOAP Message:\n".concat(soapString));
//
//        OrderCreatedRequest orderCreatedRequest = (OrderCreatedRequest) object;
//
//        saveSOAPMessage(orderCreatedRequest);
//
//        // continue on the intercepted method
//        joinPoint.proceed();
//    }
//
//    private String marshallArgument(Object object) throws JAXBException {
//        return SOAPHelper.marshaller(object);
//    }
//
//    private void saveSOAPMessage(OrderCreatedRequest soapMessage){
//
//    	
//        try {
//        	
//			 this.orderService.submitOrder(soapMessage);
//			 //this.orderService.saveProducts(soapMessage.getProducts(), soapMessage);
//			 LOGGER.info("SOAP message successfully saved into database");
//		
//        } catch (Exception e) {
//        	//Should send email from here.
//			LOGGER.error("SOAP message didn't saved into database or/and Customer and Order information didn't saved into database!");
//			e.printStackTrace();
//		}
//       
//
//    }


}
