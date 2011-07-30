<%  
  for (java.util.Iterator it = System.getProperties().keySet().iterator(); it.hasNext(); ) {
    String key = (String)it.next();
    out.println(key+"->"+ System.getProperty(key)+"<br>");
}



%>
