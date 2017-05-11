package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Wallet;
import com.diting.model.WalletLot;
import com.diting.model.WalletTransaction;
import com.diting.model.options.WalletSearchOptions;
import com.diting.resources.request.CreditWalletRequest;
import com.diting.resources.request.DebitWalletRequest;
import com.diting.service.WalletLotService;
import com.diting.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static com.diting.util.Utils.nullIfEmpty;

/**
 * AccountResource
 */
@Path("/wallets")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class WalletResource {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletLotService walletLotService;

    @POST
    @Timed
    @Path("/")
    public Wallet createWallet(Wallet wallet) {
        return walletService.create(wallet);
    }

    @PUT
    @Timed
    @Path("/update")
    public Wallet updateWallet(Wallet wallet) {
        return walletService.update(wallet);
    }

    @POST
    @Timed
    @Path("/credit")
    public WalletTransaction creditWallet(CreditWalletRequest creditRequest) {
        return walletService.credit(creditRequest);
    }

    @POST
    @Timed
    @Path("/credit-master")
    public WalletTransaction creditMasterWallet(CreditWalletRequest creditRequest) {
        return walletService.creditMaster(creditRequest);
    }

    @GET
    @Timed
    @Path("/search")
    public List<Wallet> search(@Context UriInfo uriInfo) {
        return walletService.search(buildWalletSearchOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/leger-type/{legerType}")
    public Wallet getByLegerType(@PathParam("legerType") String legerType) {
        return walletService.getWalletByLegerType(legerType);
    }

    @POST
    @Timed
    @Path("/debit")
    public WalletTransaction debitWallet(DebitWalletRequest debitRequest) {
        return walletService.debit(debitRequest);
    }

    @GET
    @Timed
    @Path("/{walletId}/lots")
    public List<WalletLot> getWalletLotsByWalletId(@PathParam("walletId") Integer walletId) {
        return walletLotService.getByWalletId(walletId);
    }

    private WalletSearchOptions buildWalletSearchOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        WalletSearchOptions options = new WalletSearchOptions();
//        buildPageableOptions(uriInfo, options);
//
        options.setUserId(nullIfEmpty(params.getFirst("userId")));
        options.setUserType(nullIfEmpty(params.getFirst("userType")));
        options.setType(nullIfEmpty(params.getFirst("type")));
        options.setLegerType(nullIfEmpty(params.getFirst("legerType")));

        return options;
    }
}
