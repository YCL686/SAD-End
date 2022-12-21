//编译器版本
pragma solidity ^0.8.0;

//导入openzeppelin库
//Ownable 是openzeppelin实现管理员
import "@openzeppelin/contracts/access/Ownable.sol";
//ERC721是NFT常用的标准协议
import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
//可以枚举
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol";

//我们创建的合约需要继承上面的合约
contract MyNFT is ERC721, ERC721Enumerable, Ownable {
    //白名单
    mapping(address => bool) public whitelist;

    bool public _isWhiteListActive = true;

    //开启售卖nft
    bool public _isSaleActive = false;

    bool public _isTransferActive = false;

    // Constants
    //设置NFT数量
    uint256 public constant MAX_SUPPLY = 8888;
    //售卖NFT的价格
    uint256 public mintPrice = 0.1 ether;
    //账户最多拥有的NFT数量
    uint256 public maxBalance = 3;
    //账户一次性最多铸造的数量
    uint256 public maxMint = 1;

    //基本url
    string private _baseURIExtended;

    event TokenMinted(uint256 supply);
    event StartTransfer();
    event StopTransfer();
    event SaleStarted();
    event SalePaused();
    event WhitelistActive();
    event WhiteListInActive();

    constructor() ERC721("SAD_AVATAR", "SADA") {}

    function () payable public {}

    //判断是否在白名单
    function isInWhitelist(address now) public view returns (bool){
        return whitelist[now];
    }

    //获取白名单状态
    function getWhiteListActive() public view returns (bool){
        return _isWhiteListActive;
    }

    //获取预售开关状态
    function getSaleActive() public view returns (bool){
        return _isSaleActive;
    }

    //获取转移状态
    function getTransferActive() public view returns (bool){
        return _isTransferActive;
    }

    //开启转移
    function startTransfer() public onlyOwner {
        _isTransferActive = true;
        emit StartTransfer();
    }

    //关闭转移
    function stopTransfer() public onlyOwner {
        _isTransferActive = false;
        emit StopTransfer();
    }

    //开始预售
    function startSale() public onlyOwner {
        _isSaleActive = true;
        emit SaleStarted();
    }

    //暂停预售
    function pauseSale() public onlyOwner {
        _isSaleActive = false;
        emit SalePaused();
    }

    //白名单生效
    function setWhiteListActive() public onlyOwner {
        _isWhiteListActive = true;
        emit WhitelistActive();
    }

    //白名单失效
    function setWhiteListInActive() public onlyOwner {
        _isWhiteListActive = false;
        emit WhiteListInActive();
    }

    //添加白名单
    function addWhitelist(address _newEntry) external onlyOwner {
        whitelist[_newEntry] = true;
    }

    //移除白名单
    function removeWhitelist(address _newEntry) external onlyOwner {
        require(whitelist[_newEntry], "Previous not in whitelist");
        whitelist[_newEntry] = false;
    }

    //设置价格
    function setMintPrice(uint256 _mintPrice) public onlyOwner {
        mintPrice = _mintPrice;
    }

    //设置拥有NFT数量
    function setMaxBalance(uint256 _maxBalance) public onlyOwner {
        maxBalance = _maxBalance;
    }

    //设置最大mint数量
    function setMaxMint(uint256 _maxMint) public onlyOwner {
        maxMint = _maxMint;
    }

    //提取合约中的ETH
    function withdraw(address to) public onlyOwner {
        uint256 balance = address(this).balance;
        payable(to).transfer(balance);
    }

    //返回当前总量
    function getTotalSupply() public view returns (uint256) {
        return totalSupply();
    }

    //获取地址拥有的nft
    function getSADAvatarByOwner(address _owner)
    public
    view
    returns (uint256[] memory)
    {
        uint256 tokenCount = balanceOf(_owner);
        uint256[] memory tokenIds = new uint256[](tokenCount);
        for (uint256 i; i < tokenCount; i++) {
            tokenIds[i] = tokenOfOwnerByIndex(_owner, i);
        }
        return tokenIds;
    }

    //mint NFT
    function mintSADAvatar(uint256 nums) public payable {
        require(_isSaleActive, "Sale must be active to mint SADAvatars");
        require(
            totalSupply() + nums <= MAX_SUPPLY,
            "Sale would exceed max supply"
        );
        require(
            balanceOf(msg.sender) + nums <= maxBalance,
            "Sale would exceed max balance"
        );
        require(nums <= maxMint, "Sale would exceed max mint");
        require(nums * mintPrice <= msg.value, "Not enough ether sent");
        // if (_isWhiteListActive) {
        //     require(whitelist[msg.sender], "Not in whitelist");
        // }
        _mintSADAvatar(nums, msg.sender);
        //emit TokenMinted(totalSupply());
    }

    //mint 函数具体实现
    function _mintSADAvatar(uint256 nums, address recipient) internal {
        uint256 mintIndex = totalSupply();
        for (uint256 i = 0; i < nums; i++) {
            _safeMint(recipient, mintIndex);
        }
    }

    //设置基本url  主要后面拼接
    function setBaseURI(string memory baseURI_) external onlyOwner {
        _baseURIExtended = baseURI_;
    }

    //获取基本url
    function _baseURI() internal view virtual override returns (string memory) {
        return _baseURIExtended;
    }

    //得到nft的url
    function tokenURI(uint256 tokenId)
    public
    view
    virtual
    override
    returns (string memory)
    {
        require(
            _exists(tokenId),
            "ERC721Metadata: URI query for nonexistent token"
        );
        return string(abi.encodePacked(_baseURI(), tokenId));
    }

    //转移这里使用了hooks
    function _beforeTokenTransfer(
        address from,
        address to,
        uint256 tokenId,
        uint256 batchSize) internal override(ERC721, ERC721Enumerable) {
        //require(_isTransferActive, "Transfer is not active");
        super._beforeTokenTransfer(from, to, tokenId, 1);
    }

    // IERC165 接口的实现
    function supportsInterface(bytes4 interfaceId)
    public
    view
    override(ERC721, ERC721Enumerable)
    returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }
}
