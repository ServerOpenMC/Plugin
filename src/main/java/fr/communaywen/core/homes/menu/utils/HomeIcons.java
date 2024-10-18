package fr.communaywen.core.homes.menu.utils;

import lombok.Getter;

@Getter
public enum HomeIcons {

    AXENQ("omc_homes:omc_homes_icon_axenq", "axenq|axeno"),
    BANK("omc_homes:omc_homes_icon_bank", "bank|banks|banque|banques|tresor|tresors|vault|vaults|coffre-fort|coffres-forts|argent|money|finance|finances|epargne|epargnes|saving|savings|treasury|treasuries|caisse|caisses"),
    CHATEAU("omc_homes:omc_homes_icon_chateau", "chateau|chateaux|castle|castles|manor|manors|palace|palaces|forteresse|forteresses|fortress|fortresses|citadelle|citadelles|citadel|citadels|donjon|donjons|keep|keeps|tour|tours|tower|towers|palais"),
    CHEST("omc_homes:omc_homes_icon_chest", "chest|chests|coffre|coffres|storage|storages|stockage|stockages|boite|boites|box|boxes|container|containers|conteneur|conteneurs|entrepot|entrepots|warehouse|warehouses|depot|depots|reserve|reserves"),
    HOME("omc_homes:omc_homes_icon_maison", "home|homes|maison|maisons|house|houses|residence|residences|domicile|domiciles|habitation|habitations|logement|logements|lodging|lodgings|dwelling|dwellings|abri|abris|shelter|shelters|foyer|foyers|hearth|hearths"),
    SANDBLOCK("omc_homes:omc_homes_icon_sandblock", "sandblock"),
    SHOP("omc_homes:omc_homes_icon_shop", "shop|shops|magasin|magasins|boutique|boutiques|store|stores|commerce|commerces|market|markets|marche|marches|epicerie|epiceries|grocery|groceries|bazaar|bazaars|emporium|emporiums|trade|trades|echange|echanges"),
    XERNAS("omc_homes:omc_homes_icon_xernas", "xernas"),
    FARM("omc_homes:omc_homes_icon_zombie", "farm|farms|ferme|fermes|culture|cultures|plantation|plantations|agriculture|champ|champs|field|fields|recolte|recoltes|harvest|harvests|elevage|elevages|breeding|ranch|ranches|grange|granges|barn|barns"),
    DEFAULT("omc_homes:omc_homes_icon_grass", "default")
    ;

    private final String id;
    private final String usage;

    HomeIcons(String id, String usage) {
        this.id = id;
        this.usage = usage;
    }
}
