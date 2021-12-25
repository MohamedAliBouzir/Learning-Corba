/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.application;

import AdditionApp.Addition;
import AdditionApp.AdditionHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

/**
 *
 * @author MSI
 */
public class ServerApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
        // create and initialize the ORB (object Request Brod)//// get reference to rootpoa &amp; activate the POAManager
        ORB orb = ORB.init(args, null); 
        // Instancie et initialise l'ORB
        // obtention d'une référence sur le POA racine 
        // un adapteur d'objet est un mécanisme qui connecte une requête 
        // (utilisant une référence à un object) avec le code du service requis.
        POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootpoa.the_POAManager().activate(); // activation du gestionnaire de POA qui devient prêt à traiter une requête
        // create servant and register it with the ORB
        // Intancie le servant (l'objet requis) et l'enregistre auprès de l'ORB
        // Le servant est une instance de la classe AdditionObj
        // qui hérite de l'adaptateur d'objets portables sur différents ORB(classe AdditionPOA)
        AdditionObj addobj = new AdditionObj(); 
        addobj.setORB(orb); 

        // get object reference from the servant
        org.omg.CORBA.Object ref = rootpoa.servant_to_reference(addobj);
        Addition href = AdditionHelper.narrow(ref);

        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

        NameComponent path[] = ncRef.to_name( "ABC" );

        ncRef.rebind(path, href);
        
        //en cas de succes de lancement: on va obtenir ce message
        System.out.println("Addition Server ready and waiting ...");

        // wait for invocations from clients
        for (;;){
        orb.run();
        }
        } 
        catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
        System.out.println("HelloServer Exiting ...");

        }
       }
 }
